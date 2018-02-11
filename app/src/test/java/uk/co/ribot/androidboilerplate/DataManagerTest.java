package uk.co.ribot.androidboilerplate;

import android.app.Application;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.AuthResource;
import uk.co.ribot.androidboilerplate.data.remote.TaskResource;

import static org.mockito.Mockito.verify;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock TaskResource mMockTaskResource;
    @Mock AuthResource mAuthResource;
    @Mock Application mApplication;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockTaskResource,
                mAuthResource,
                mMockPreferencesHelper,
                mMockDatabaseHelper);
    }

//    @Test
//    public void syncRibotsEmitsValues() {
//        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
//                TestDataFactory.makeRibot("r2"));
//        stubSyncRibotsHelperCalls(ribots);
//
//        TestObserver<Ribot> result = new TestObserver<>();
//        mDataManager.syncRibots().subscribe(result);
//        result.assertNoErrors();
//        result.assertValueSequence(ribots);
//    }
//
//    @Test
//    public void syncRibotsCallsApiAndDatabase() {
//        List<Ribot> ribots = Arrays.asList(TestDataFactory.makeRibot("r1"),
//                TestDataFactory.makeRibot("r2"));
//        stubSyncRibotsHelperCalls(ribots);
//
//        mDataManager.syncRibots().subscribe();
//        // Verify right calls to helper methods
//        verify(mMockTaskResource).getTasks();
//        verify(mMockDatabaseHelper).setTasks(ribots);
//    }
//
//    @Test
//    public void syncRibotsDoesNotCallDatabaseWhenApiFails() {
//        when(mMockTaskResource.getTasks())
//                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));
//
//        mDataManager.syncRibots().subscribe(new TestObserver<Ribot>());
//        // Verify right calls to helper methods
//        verify(mMockTaskResource).getTasks();
//        verify(mMockDatabaseHelper, never()).setTasks(ArgumentMatchers.<Ribot>anyList());
//    }

//    private void stubSyncRibotsHelperCalls(List<Ribot> ribots) {
//        // Stub calls to the ribot service and database helper.
//        when(mMockTaskResource.getTasks())
//                .thenReturn(Observable.just(ribots));
//        when(mMockDatabaseHelper.setTasks(ribots))
//                .thenReturn(Observable.fromIterable(ribots));
//    }

}
