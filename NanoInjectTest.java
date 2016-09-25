
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

public class NanoInjectTest {

	public interface IApp {

	}

	public static class App implements IApp {

	}

	public static class NoUse implements IApp {

	}

	@Test
    public void test() {
        assertEquals(1, 1);
    }

    @Test
    public void testInit() {
    	NanoInject.Builder builder = new NanoInject.Builder();
    	builder.bind(IApp.class, App.class);
    	NanoInject.init(builder);

    	assertNotNull(NanoInject.instance());
	}

    @Test
    public void testGet0() {
    	NanoInject.Builder builder = new NanoInject.Builder();
    	builder.bind(IApp.class, App.class);
    	NanoInject.init(builder);

    	IApp app = NanoInject.instance().get(IApp.class);
    	assertNotNull(app);
    	assertEquals(app.getClass(), App.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGet1() {
    	NanoInject.Builder builder = new NanoInject.Builder();
    	builder.bind(IApp.class, App.class);
    	NanoInject.init(builder);

    	IApp app = NanoInject.instance().get(NoUse.class);
    	assertNotNull(app);
    }
}
