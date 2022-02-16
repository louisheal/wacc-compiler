import java.security.Permission;

public class MySecurityManager extends SecurityManager {

  @Override
  public void checkExit(int status) {
    if (status == 100 || status == 200) {
      throw new SecurityException();
    }
  }

  @Override
  public void checkPermission(Permission perm) {
    // Allow other activities by default
  }

}
