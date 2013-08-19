package jetbrains.buildserver.environmentFetcher.agent;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.PathUtil;
import java.io.File;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.BuildAgent;
import jetbrains.buildServer.util.EventDispatcher;

/**
 * @author Roman.Chernyatchik
 */
public class EnvironmentFetcherService {
  public static final String TEAMCITY_CAPTURE_ENV_NAME = "TEAMCITY_CAPTURE_ENV";

  public EnvironmentFetcherService(final EventDispatcher<AgentLifeCycleListener> dispatcher) {
    dispatcher.addListener(new MyAgentLifeCycleListener());
  }

  /**
   * @author Roman.Chernyatchik
   */
  public static class MyAgentLifeCycleListener extends AgentLifeCycleAdapter {
    @Override
    public void beforeAgentConfigurationLoaded(@org.jetbrains.annotations.NotNull final BuildAgent agent) {
      agent.getConfiguration().addEnvironmentVariable(TEAMCITY_CAPTURE_ENV_NAME, generateCmdLine());
    }

    public String generateCmdLine() {
      final StringBuilder buf = new StringBuilder();

      // java executable path
      buf.append(System.getProperty("java.home")).append(File.separator);
      buf.append("bin").append(File.separator).append(SystemInfo.isWindows ? "java.exe" : "java");

      // -jar option
      buf.append(" -jar ");

      // jar file path
      final String thisJarPath = PathUtil.getJarPathForClass(EnvironmentFetcherService.class);
      final String pluginLibDir = PathUtil.getParentPath(thisJarPath);
      final String pluginHomeDir = PathUtil.getParentPath(pluginLibDir);

      buf.append(pluginHomeDir).append(File.separatorChar)
        .append("bin").append(File.separatorChar)
        .append("env-fetcher.jar");

      return buf.toString();
    }
  }
}
