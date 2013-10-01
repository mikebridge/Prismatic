package com.bridgecanada.prismatic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.data.*;
import com.bridgecanada.prismatic.data.PrismaticFeed;
import com.bridgecanada.prismatic.feed.*;
import com.bridgecanada.prismatic.queue.MessageHandler;
import com.bridgecanada.prismatic.ui.CardListFragment;
import com.bridgecanada.prismatic.ui.DrawerItem;
import com.bridgecanada.prismatic.ui.DrawerItemAdapter;
import com.google.inject.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import roboguice.activity.RoboFragmentActivity;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bridgecanada.prismatic.feed.FeedCache.*;

public class MainActivity extends RoboFragmentActivity {

    private final String TAG = getClass().getSimpleName();
    private ActionBarDrawerToggle _actionBarDrawerToggle;

    private final int LOGIN_RESULT_ID = 1; // communicate with Login Activity
    private String _feedId = null; // the current feed id (TODO: Save state?)
    //private Next _next = null; // the next articles to retrieve (TODO: Save state?)
    private long _first = 0;
    private long _last = 0;
    private long _start = 0; // the start parameter from last feed call (TODO: Save state?)


    private IFeedStrategy _currentStrategy = null;

    private HandlerThread _handlerThread;

    @Inject HomeStrategy.HomeStrategyFactory _homeStrategyProvider;
    @Inject PersonalKeyStrategy.PersonalKeyStrategyFactory _personalKeyStrategyProvider;
    @Inject SharedFeedStrategy.SharedFeedStrategyFactory _sharedFeedStrategyProvider;
    @Inject SavedFeedStrategy.SavedFeedStrategyFactory _savedFeedStrategyProvider;
    @Inject ReadFeedStrategy.ReadFeedStrategyFactory _readFeedStrategyProvider;
    @Inject RecommendedFeedStrategy.RecommendedFeedStrategyFactory _recommendedFeedStrategyProvider;
    @Inject IPrismaticFeed _prismaticFeed;
    //@Inject DispatchServiceBase _dispatchService;
    //@Inject DispatchServiceBase _eventDispatchService;
    @Inject IFeedCache _feedCache;
    @Inject DispatchServiceBase.IDispatchServiceBaseFactory _dispatchPaymentFactory;
    @Inject private IAuthService _authService;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        createEventQueue();

        setContentView(R.layout.main);

        addCardFragmentListeners();

        addDrawer();

        setCurrentStrategy(_personalKeyStrategyProvider.create());

        loadFeed(getCurrentStrategy());

    }

    // TODO: inject this
    private void createEventQueue() {

        _handlerThread = new HandlerThread(TAG);
        _handlerThread.start();

    }

    private void addDrawer() {

        //new DrawerLayout();
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new DrawerItemAdapter(this, getDrawerMenuItems()));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        SetUpDrawerToggle(drawerLayout);

    }

    private void SetUpDrawerToggle(DrawerLayout drawerLayout) {

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        _actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,          /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("Closed Drawer");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Opened Drawer");
            }
        };

        // Show the updated menu icon.
        // http://stackoverflow.com/questions/17030798/navigation-drawer-icon-not-showing-sherlock-actionbar
        _actionBarDrawerToggle.syncState();

    }

    private List<DrawerItem> getDrawerMenuItems() {
        //asList("all", "shared", "recommended", "saved")
        List<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem().setTitle("all").setFeedStrategy(_personalKeyStrategyProvider.create()));
        items.add(new DrawerItem().setTitle("shared").setFeedStrategy(_sharedFeedStrategyProvider.create(0)));
        items.add(new DrawerItem().setTitle("recommended").setFeedStrategy(_recommendedFeedStrategyProvider.create(0)));
        items.add(new DrawerItem().setTitle("saved").setFeedStrategy(_savedFeedStrategyProvider.create(0)));
        items.add(new DrawerItem().setTitle("read").setFeedStrategy(_readFeedStrategyProvider.create(0)));
        return items;
    }

    private void addCardFragmentListeners() {

        getCardListFragment().addOnScrollToEndListener(new ScrollToEndListener());
        getCardListFragment().addOnItemClickedListener(new ItemClickedListener());

    }

    private void loadFeed(IFeedStrategy feedStrategy) {

        _prismaticFeed.GetFeed(feedStrategy, getFeedSuccessCallback(), getFeedFailureCallback());

    }

    private void setCurrentStrategy(IFeedStrategy feedStrategy) {
        _currentStrategy = feedStrategy;

    }
    private IFeedStrategy getCurrentStrategy() {


        return _currentStrategy;

    }


    private void clearItems() {
        getCardListFragment().clearItems();
    }

    private CardListFragment getCardListFragment() {
        return (CardListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.list_fragment);
    }

    /**
     * There is no auth info (or it's invalid); start loginactivity
     * @param errorResult
     */
    void processFailure(HttpError errorResult) {

        if (errorResult.needsAuthentication()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_RESULT_ID);
        } else {
            showErrorMessage(errorResult.getErrorMessage());

        }
    }

    /**
     * On login
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == LOGIN_RESULT_ID) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra("url");
                Log.i(TAG, "URL: " + url);
                loadFeed(getCurrentStrategy());

                // use 'myValue' return value here
            }
        }
    }


    /**
     * Show an error message
     * TODO: Make this more like a flash message.
     * @param message
     */
    private void showErrorMessage(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, message, duration);
        toast.show();

    }


    private IHttpResultCallback<PrismaticFeed> getFeedSuccessCallback() {


        return new IHttpResultCallback<PrismaticFeed>() {

            /**
             * Update the card fragment;
             * Side-effect: update the _feedId and _next with
             * the parameters to be used on the next call.
             * @return
             */
            @Override
            public void onComplete(PrismaticFeed result, String jsonResult, int statusCode) {

                _feedId = result.getId();

                //_next = result.getNext().getQueryParams();
                _first = 0;
                _last = 0;
                if (result.getNext()!=null && result.getNext().getQueryParams()!= null) {
                    _first = result.getNext().getQueryParams().getFirstArticleIdx();
                    _last = result.getNext().getQueryParams().getLastArticleIdx();
                    _start = result.getNext().getQueryParams().getStart();
                }


                //Toast.makeText(getApplicationContext(), "Start "+_start+ " first "+_first+ "last "+_last, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Start "+_start+ "/first "+_first+ "/last "+_last);

                CardListFragment cardListFragment = getCardListFragment();

                if (cardListFragment == null) {
                    // this seems to happen if we destroy the activity
                    // and reload it.
                    return;
                }

                for (Doc doc: result.getDocs()) {

                    CardData newCard = new CardData();
                    UpdateCardFromDoc(doc, newCard);

                    cardListFragment.addItem(newCard);

                }
                cacheDocs(_feedId, _start, _first, _last, jsonResult);
                //System.out.println(TAG + ".onSuccess: " + result);

            }


        };
    }

    // cache the docs in the background
    private void cacheDocs(String feedId, long start, long first, long last, String json) {
        FeedData feedData = new FeedData();
        //feedData.
        MessageHandler messageHandler = new MessageHandler(_handlerThread.getLooper());
        messageHandler.post(new CacheFeed(feedId, feedData, json));

    }

    private class CacheFeed implements Runnable {
        private String feedId;
        private FeedData feedData;
        private String json;


        public CacheFeed(String feedId, FeedData feedData, String json) {
            this.json = json;
            this.feedId = feedId;
            this.feedData = feedData;
        }

        @Override
        public void run() {

            try {
                Log.i(TAG, "*** RUNNING FEED CACHE FROM id"+Thread.currentThread().getId());
                _feedCache.saveManyDocs(feedId, feedData, json);
            }
            catch (IOException ex) {
                Log.e(TAG, "Error saving to cache: "+ex.getMessage());

            }
        }
    }

    private void UpdateCardFromDoc(Doc doc, CardData newCard) {

        // todo: create adapter for this
        newCard.setTitle(doc.getTitle());
        //newCard.setTopics()

        Img img = doc.getImg(); // Doc description
        if (img !=null && img.getUrl() != null) {

            Image image = new Image(); // image view
            image.setUrl(img.getUrl());

            // TODO: if width or height is zero, the
            // cacher will throw an error.
            if (img.getSize() !=null) {
                image.setHeight(img.getSize().getHeight());
                image.setWidth(img.getSize().getWidth());
            }

            newCard.setImg(image);
        }
        newCard.setId(doc.getId());
        newCard.setAuthor(doc.getAuthor());
        newCard.setDate(doc.getDate());
        newCard.setUrl(doc.getUrl());
        newCard.setText(doc.getText());

        newCard.setPrismaticActivity(doc.getPrismaticActivity());

        if (doc.getFeed() != null) {
            newCard.setFeedName(doc.getFeed().getTitle());
            newCard.setFeedUrl(doc.getFeed().getUrl());
            newCard.setFeedHighResImage(doc.getFeed().getHighResImage());
            newCard.setFeedImage(doc.getFeed().getImage());

        }
    }

    private IHttpResultCallback<HttpError> getFeedFailureCallback() {

        return new IHttpResultCallback<HttpError>() {

            @Override
            public void onComplete(HttpError errorResult, String rawResult, int statusCode) {
                //if (errorResult.getStatus() == 401 || errorResult.getStatus()== 403)
                //Toast.makeText(t, "Something bad happened!", Toast.LENGTH_SHORT).show();
                //System.out.println("Error "+errorResult.getErrorMessage());
                processFailure(errorResult);

            }

        };
    }


    private class ScrollToEndListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            //showErrorMessage("scrolled to end!");
            //Log.i(TAG, "***  HEARD LOGGED TO END, refreshing! " + propertyChangeEvent.getNewValue());
            setNextStrategy();
            loadFeed(getCurrentStrategy());
        }

    }

    /**
     * Ask the old feed what hte next one shoudl be.
     */
    private void setNextStrategy() {
        setCurrentStrategy(getCurrentStrategy().next(_feedId,
                _start,
                _first,
                _last));
    }

    private class ItemClickedListener implements PropertyChangeListener {

        @Override
        /**
         * User clicked something---launch the browser
         */
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            //String url = (String) propertyChangeEvent.getNewValue();
            CardData cardData = (CardData) propertyChangeEvent.getNewValue();
            String url = cardData.getUrl();
            //showErrorMessage("clicked on " + propertyChangeEvent.getNewValue());
            Uri uri = Uri.parse(url);

            // log the activity

            // TODO: Map the current feed's url
            String sourceUrlPath = getCurrentStrategy().getUrlPath(); // e,g, "/news/home"
            String idStr = String.format("%d", cardData.getId());

            EventDispatchService dispatchService = _dispatchPaymentFactory.createEventDispatch(idStr);
            dispatchService.sendConfirmation(getDispatchSuccessCallback(), getDispatchFailureCallback());


            AuthEventPublicDispatch eventDispatchService = _dispatchPaymentFactory.createDispatch(sourceUrlPath, uri.getPath(), url);
            eventDispatchService.sendConfirmation(getEventDispatchSuccessCallback(), getEventDispatchFailureCallback());

            Intent browserLaunchIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browserLaunchIntent);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        /**
         * Click in drawer menu
         */
        public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
            DrawerItem item = (DrawerItem) parent.getItemAtPosition(i);
            //Toast toast = Toast.makeText(getApplicationContext(), "clicked on "+item.getTitle(), Toast.LENGTH_SHORT);
            //toast.show();

            if (item.getFeedStrategy() == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "not hooked up yet: "+item.getTitle(), Toast.LENGTH_SHORT);

            } else {
                clearItems();
                setCurrentStrategy(item.getFeedStrategy()); // reset with new strategy
                //Toast toast = Toast.makeText(getApplicationContext(), "loading "+strategy.getFeedUrl(), Toast.LENGTH_SHORT);
                //toast.show();
                loadFeed(getCurrentStrategy());

            }
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawers();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event


        if (_actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId()) {
                case R.id.item_logout:
                    LogOut();
                    return true;
                case R.id.item_about:
                    showErrorMessage("Clicked About");
                    return true;
                case R.id.item_refresh:
                    Reload();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
        //}
        // Handle other action bar items...
        //return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        _authService.Logoff();
        this.recreate();
    }

    private void Reload() {
        this.recreate();
    }



    private IHttpResultCallback<JsonNode>  getDispatchFailureCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node, String rawResult, int statusCode) {
                Log.e(TAG," failure dispatching: ");
                try {
                    String failureJson = new ObjectMapper().writeValueAsString(node);
                    Log.e(TAG,failureJson);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
        };
    }

    private IHttpResultCallback<JsonNode>  getDispatchSuccessCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node,String rawResult, int statusCode) {
                Log.i(TAG," Success dispatching: ");
                try {
                    String successJson = new ObjectMapper().writeValueAsString(node);
                    Log.i(TAG,successJson);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };
    }

    private IHttpResultCallback<JsonNode>  getEventDispatchFailureCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node, String rawResult, int statusCode) {
                Log.e(TAG," failure event dispatching: ");
                try {
                    String failureJson = new ObjectMapper().writeValueAsString(node);
                    Log.e(TAG,failureJson);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
        };
    }

    private IHttpResultCallback<JsonNode>  getEventDispatchSuccessCallback() {
        return new IHttpResultCallback<JsonNode> () {

            @Override
            public void onComplete(JsonNode node,String rawResult, int statusCode) {
                Log.i(TAG," Success event dispatching: ");
                try {
                    String successJson = new ObjectMapper().writeValueAsString(node);
                    Log.i(TAG,successJson);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };
    }

    /*
    // this is an experiment to add items...
    public void addOne(View view)
    {
        CardListFragment cardListFragment = getCardListFragment();


        CardData testData = new CardData();
        testData.setTitle("Test Item "+ new Random().nextInt());

        cardListFragment.addItem(testData);

        if (cardListFragment != null) {


            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            //reportFragment.updateArticleView(position);
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

//            // Create fragment and give it an argument for the selected article
//            ReportFragment newFragment = new ReportFragment();
//            Bundle args = new Bundle();
//            args.putInt(ReportFragment.ARG_POSITION, position);
//            newFragment.setArguments(args);
//
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            //transaction.replace(R.id.fragment_container, newFragment);
            //transaction.addToBackStack(null);

            // Commit the transaction
            //transaction.commit();
        }
    }
        */
}
