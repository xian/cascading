package cascading.operation.xml;

import cascading.operation.Function;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;
import cascading.CascadingTestCase;
import org.junit.Test;

public class XPathParserTest extends CascadingTestCase {
  @Test
  public void testOperateWithMultipleXPaths() {
    String[][] namespaces = {
        new String[]{"a", "http://schemas.xxx.com/XXX-A.v1"},
        new String[]{"p", "http://schemas.xxx.com/XXX-P.v1"}
    };
    String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
        "<msg xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.XXX.com/XXX\">\n" +
        "    <p:Product xmlns:a=\"http://schemas.xxx.com/XXX-A.v1\" xmlns:p=\"http://schemas.xxx.com/XXX-P.v1\">" +
        "    <a:GAC>gacValue</a:GAC>" +
        "    <a:Owner><p:CompanyId>other thing</p:CompanyId></a:Owner>" +
        "    </p:Product>" +
        "</msg>";

    Function function = new XPathParser(new Fields("gac", "companyId"), namespaces,
        "//p:Product/a:GAC/text()",
        "//a:Owner/p:CompanyId/text()");
    Tuple tuple = invokeFunction(function, new Tuple(xml), new Fields("gac", "companyId")).iterator().next();
    assertEquals("gacValue", tuple.getString(0));
    assertEquals("other thing", tuple.getString(1));
  }
}
