package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

public abstract class RestClient {

    protected static final Config CFG = Config.getInstance();

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), HEADERS, null);
    }

    public RestClient(String baseUrl, Boolean followRedirects) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), HEADERS, null);
    }

    public RestClient(String baseUrl, Converter.Factory converterFactory, Boolean followRedirects) {
        this(baseUrl, followRedirects, converterFactory, HEADERS, null);
    }

    public RestClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel, null);
    }

    public RestClient(String baseUrl, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, converterFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, Boolean followRedirects, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirects, converterFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, Boolean followRedirects, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, @Nullable Interceptor... interceptors) {
        OkHttpClient.Builder oKHttpBuilder = new OkHttpClient.Builder()
                .followRedirects(followRedirects);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                oKHttpBuilder.addNetworkInterceptor(interceptor);
            }
        }
        oKHttpBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel));
        this.okHttpClient = oKHttpBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build();
    }
}
