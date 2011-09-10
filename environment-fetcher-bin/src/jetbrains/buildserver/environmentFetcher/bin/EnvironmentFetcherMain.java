package jetbrains.buildserver.environmentFetcher.bin;

import java.util.Map;
import jetbrains.buildServer.messages.serviceMessages.MapSerializerUtil;

/**
 * @author Roman.Chernyatchik
 */
public class EnvironmentFetcherMain {
  public static void main(String[] args) {
    // get env:
    final Map<String,String> env = System.getenv();

    // pass using service messages:
    for (Map.Entry<String, String> entry : env.entrySet()) {
      final String key = MapSerializerUtil.escapeStr(entry.getKey(), MapSerializerUtil.STD_ESCAPER);
      final String value = MapSerializerUtil.escapeStr(entry.getValue(), MapSerializerUtil.STD_ESCAPER);

      System.out.println("##teamcity[buildEnvironment name='" + key + "' value='" + value + "']");
    }
  }
}
