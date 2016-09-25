
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * abstract of class/interface and so on
 *
 * @author kailun on 16/4/19
 */
public class NanoInject {

    private static class Binder<T> {
        Class<T> bindClass;
        Class<? extends T> toClass;
        Provider<T> provider;
        T toInstance;
    }

    public static class Builder {

        List<Binder> binders = new ArrayList<>();

        public <T> void bind(Class<T> bindClass, Class<? extends T> toClass) {
            Binder<T> binder = new Binder<T>();
            binder.bindClass = bindClass;
            binder.toClass = toClass;

            binders.add(binder);
        }

        public <T> void bindInstance(Class<T> bindClass, T toInstance) {
            Binder<T> binder = new Binder<T>();
            binder.bindClass = bindClass;
            binder.toInstance = toInstance;

            binders.add(binder);
        }

        public <T> void bindProvider(Class<T> bindClass, Provider<T> provider) {
            Binder<T> binder = new Binder<T>();
            binder.bindClass = bindClass;
            binder.provider = provider;

            binders.add(binder);
        }
    }

    public interface Provider<T> {
        T get();
    }

    private static NanoInject sInstance;
    public static NanoInject instance() {
        if (sInstance == null) {
            throw new RuntimeException("NanoInject not inited");
        }
        return sInstance;
    }

    public static void init(Builder builder) {
        NanoInject inject = new NanoInject();
        inject.initInject(builder);
        sInstance = inject;
    }

    private HashMap<Class, Class> classBinders;
    private HashMap<Class, Object> instanceBinders;
    private HashMap<Class, Provider<?>> providerBinders;
    {
        classBinders = new HashMap<>();
        instanceBinders = new HashMap<>();
        providerBinders = new HashMap<>();
    }

    private NanoInject() {
    }

    private void initInject(Builder builder) {
        List<Binder> binders = builder.binders;
        for (Binder b: binders) {
            if (b.toClass != null) {
                classBinders.put(b.bindClass, b.toClass);
            } else if (b.toInstance != null) {
                instanceBinders.put(b.bindClass, b.toInstance);
            } else if (b.provider != null) {
                providerBinders.put(b.bindClass, b.provider);
            }
            else {
                throw new RuntimeException("toClass and toInstance both null");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> bindClass) {
        if (instanceBinders.containsKey(bindClass)) {
            T toInstance = (T) instanceBinders.get(bindClass);
            if (toInstance == null) {
                throw new RuntimeException("cant resolve dependcy: " + bindClass.getName());
            }

            return toInstance;
        }
        else if (classBinders.containsKey(bindClass)) {
            Class<T> toClass = classBinders.get(bindClass);
            if (toClass == null) {
                throw new RuntimeException("cant resolve dependcy: " + bindClass.getName());
            }

            try {
                return (T) toClass.newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        } else if (providerBinders.containsKey(bindClass)) {
            Provider<T> provider = (Provider<T>) providerBinders.get(bindClass);
            if (provider == null) {
                throw new RuntimeException("cant resolve dependcy: " + bindClass.getName());
            }

            return provider.get();
        } else {
            throw new RuntimeException("cant find bindClass: " + bindClass.getName());
        }
    }
}
