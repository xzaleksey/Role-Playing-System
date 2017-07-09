package com.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaHooks;

public class SchedulersRule implements TestRule {
  private final Scheduler scheduler;

  public SchedulersRule(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        Scheduler correctScheduler = SchedulersRule.this.scheduler;
        RxAndroidPlugins rxAndroidPlugins = RxAndroidPlugins.getInstance();
        rxAndroidPlugins.reset();
        rxAndroidPlugins.registerSchedulersHook(new RxAndroidSchedulersHook(){
          @Override public Scheduler getMainThreadScheduler() {
            return correctScheduler;
          }
        });
        RxJavaHooks.setOnIOScheduler(scheduler -> correctScheduler);
        RxJavaHooks.setOnComputationScheduler(scheduler -> correctScheduler);
        RxJavaHooks.setOnNewThreadScheduler(scheduler -> correctScheduler);
        try {
          base.evaluate();
        } finally {
          RxJavaHooks.reset();
        }
      }
    };
  }
}
      