package uk.co.ribot.androidboilerplate.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final int retryDelaySeconds;
    private int retryCount;

    /* maxRetries = 0 -> unlimited */
    public RetryWithDelay(final int maxRetries, final int retryDelaySeconds) {
        this.maxRetries = maxRetries;
        this.retryDelaySeconds = retryDelaySeconds;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(final Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Function<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> apply(final Throwable throwable) {
                        if (maxRetries == 0 || ++retryCount < maxRetries) {
                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).

                            Timber.i("retryDelaySeconds");
                            return Observable.timer(retryDelaySeconds,
                                    TimeUnit.SECONDS);
                        }

                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}