package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             String friend,
                             String income,
                             String outcome) {
    }

    private final static Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("gale.bernier", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "gale.bernier", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("user", "password", null, "john", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("admin", "12345", null, null, "kyle"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(
                        p -> {
                            UserType ut = p.getAnnotation(UserType.class);
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();

                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(getUserFromQueue(ut).poll());
                            }

                            Allure.getLifecycle().updateTestCase(
                                    tc -> {
                                        tc.setStart(new Date().getTime());
                                    }
                            );

                            user.ifPresentOrElse(
                                    u -> {
                                        ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                .getOrComputeIfAbsent(
                                                        context.getUniqueId(),
                                                        key -> new HashMap<>()
                                                )).put(ut, u);
                                    },
                                    () -> {
                                        throw new IllegalStateException("No users available after 30 sec");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class);

        for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
            UserType type = e.getKey();
            getUserFromQueue(type).add(e.getValue());
        }
    }

    private static Queue<StaticUser> getUserFromQueue(UserType type) {
        return switch (type.value()) {
            case UserType.Type.EMPTY -> EMPTY_USERS;
            case UserType.Type.WITH_FRIEND -> WITH_FRIEND_USERS;
            case UserType.Type.WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case UserType.Type.WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
