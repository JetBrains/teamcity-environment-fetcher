package jetbrains.buildserver.environmentFetcher.agent;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.PathUtil;
import java.io.File;
import java.util.Map;
import jetbrains.buildServer.ExtensionHolder;
import jetbrains.buildServer.agent.config.AgentConfigurationAdapter;
import jetbrains.buildServer.agent.config.AgentConfigurationSnapshot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Roman.Chernyatchik
 */
public class EnvironmentFetcherService {
  public static final String TEAMCITY_CAPTURE_ENV_NAME = "TEAMCITY_CAPTURE_ENV";

  public EnvironmentFetcherService(@NotNull final ExtensionHolder extensionHolder) {
    extensionHolder.registerExtension(AgentConfigurationSnapshot.class, getClass().getName(), new MyAgentConfigurationSnapshot());
  }

  /**
   * @author Roman.Chernyatchik
   */
  public static class MyAgentConfigurationSnapshot extends AgentConfigurationAdapter {
    @Override
    public void addEnvironmentVariables(@NotNull Map<String, String> environmentVariables) {
      environmentVariables.put(TEAMCITY_CAPTURE_ENV_NAME, generateCmdLine());
    }

    public String generateCmdLine() {
      final StringBuilder buf = new StringBuilder();

      // java executable path
      buf.append("\"");
      buf.append(System.getProperty("java.home")).append(File.separator);
      buf.append("bin").append(File.separator).append(SystemInfo.isWindows ? "java.exe" : "java");
      buf.append("\"");

      // -jar option
      buf.append(" -jar ");

      // jar file path
      final String thisJarPath = PathUtil.getJarPathForClass(EnvironmentFetcherService.class);
      final String pluginLibDir = PathUtil.getParentPath(thisJarPath);
      final String pluginHomeDir = PathUtil.getParentPath(pluginLibDir);

      buf.append("\"");
      buf.append(pluginHomeDir).append(File.separatorChar)
        .append("bin").append(File.separatorChar)
        .append("env-fetcher.jar");
      buf.append("\"");

      return buf.toString();
    }
  }
}
