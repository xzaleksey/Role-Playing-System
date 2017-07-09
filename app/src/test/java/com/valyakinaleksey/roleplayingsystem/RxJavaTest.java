package com.valyakinaleksey.roleplayingsystem;

import com.rules.SchedulersRule;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import timber.log.Timber;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class RxJavaTest {
  private static final List<String> WORDS =
      Arrays.asList("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog");
  private final TestScheduler scheduler = new TestScheduler();
  @Rule public final SchedulersRule schedulers = new SchedulersRule(scheduler);

  @Test
  public void testUsingImmediateSchedulersRule() {
    // given:
    TestSubscriber<String> subscriber = new TestSubscriber<>();
    Observable<String> observable = Observable.from(WORDS)
        .zipWith(Observable.range(1, Integer.MAX_VALUE),
            (string, index) -> String.format("%2d. %s", index, string));

    // when:
    observable.subscribeOn(Schedulers.computation())
        .subscribe(subscriber);

    // then:
    scheduler.triggerActions();
    subscriber.assertNoErrors();
    subscriber.assertCompleted();
    subscriber.assertValueCount(9);
    List<String> onNextEvents = subscriber.getOnNextEvents();
    Timber.d(onNextEvents.toString());
    assertThat(onNextEvents, hasItem(" 4. fox"));
  }
}
      