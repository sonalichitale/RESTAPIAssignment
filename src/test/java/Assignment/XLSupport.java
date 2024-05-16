package Assignment;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Assignment.Assignment1;

public class XLSupport {
    String testname = null;

    Assignment1 obj;

   // @BeforeClass
    public void setup()
    {
        obj =new Assignment1();
        //System.out.println("test");
    }

    //@Test
    public void do_all_web_app_test()
    {
        String NameXL =obj.read_And_Print_XL_AsPerTestData("Name");
    }

}
