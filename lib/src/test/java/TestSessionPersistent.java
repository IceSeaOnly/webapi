import org.junit.Test;

public class TestSessionPersistent {
    @Test
    public void test(){
        TestEntity entity = new TestEntity();
        System.out.println(entity.sessionTag());
    }
}
