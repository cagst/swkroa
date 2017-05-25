package com.cagst.swkroa.system;

import java.util.List;

/**
 * Definition of a repository that retrieves and persists {@link SystemSetting} objects.
 *
 * @author Craig Gaskill
 */
public interface SystemRepository {
  List<SystemSetting> getSystemSettings();
}
