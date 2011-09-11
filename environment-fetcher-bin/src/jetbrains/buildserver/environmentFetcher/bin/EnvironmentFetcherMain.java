package jetbrains.buildserver.environmentFetcher.bin;

import java.util.LinkedHashMap;
import java.util.Map;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage;

/**
 * @author Roman.Chernyatchik
 */
public class EnvironmentFetcherMain {
  public static void main(String[] args) {
    // get env:
    final Map<String,String> env = System.getenv();

    // pass using service messages:
    for (Map.Entry<String, String> entry : env.entrySet()) {
      final Map<String, String> params = new LinkedHashMap<String, String>();
      params.put("name", entry.getKey());
      params.put("value", entry.getValue());
      System.out.println(ServiceMessage.asString("buildEnvironment", params));
    }
    System.out.flush();
  }
}
