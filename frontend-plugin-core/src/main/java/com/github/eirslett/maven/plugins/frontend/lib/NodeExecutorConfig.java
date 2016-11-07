package com.github.eirslett.maven.plugins.frontend.lib;

import java.io.File;

public interface NodeExecutorConfig {
  File getNodePath();
  File getNpmPath();
  File getWorkingDirectory();
  Platform getPlatform();
}

final class InstallNodeExecutorConfig implements NodeExecutorConfig {

  private static final String NODE_WINDOWS = NodeInstaller.INSTALL_PATH.replaceAll("/", "\\\\") + "\\node.exe";
  private static final String NODE_DEFAULT = NodeInstaller.INSTALL_PATH + "/node";
  private static final String NPM = NodeInstaller.INSTALL_PATH + "/node_modules/npm/bin/npm-cli.js";
  private static final String NPM_NEW = NodeInstaller.INSTALL_PATH + "/node_modules/%s/bin/npm-cli.js";
  private static final String NPM_PARENT = NodeInstaller.INSTALL_PATH + "/node_modules/";

  private final InstallConfig installConfig;

  public InstallNodeExecutorConfig(InstallConfig installConfig) {
    this.installConfig = installConfig;
  }

  @Override
  public File getNodePath() {
    String nodeExecutable = getPlatform().isWindows() ? NODE_WINDOWS : NODE_DEFAULT;
    return new File(installConfig.getInstallDirectory() + nodeExecutable);
  }

  @Override
  public File getNpmPath() {
    File file = new File(installConfig.getInstallDirectory() + Utils.normalize(NPM_PARENT));
    if (file != null && file.exists()) {
      String[] list = file.list();
      String npmDirName = null;
      for (String fileName: list) {
        if (fileName.matches("^npm-[0-9\\.]+$")) {
          npmDirName = fileName;
          break;
        }
      }

      if (npmDirName != null) {
        return new File(installConfig.getInstallDirectory() + Utils.normalize(String.format(NPM_NEW, npmDirName)));
      }
    }

    return new File(installConfig.getInstallDirectory() + Utils.normalize(NPM));
  }


  @Override
  public File getWorkingDirectory() {
    return installConfig.getWorkingDirectory();
  }

  @Override
  public Platform getPlatform() {
    return installConfig.getPlatform();
  }
}