package com.github.wreulicke.reflect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class MethodTest {
  public static class MethodDummy {
    public boolean publicMethod() {
      return true;
    }

    private boolean privateMethod() {
      return false;
    }
  }

  public void target() {}

  @Test
  void test0() throws NoSuchMethodException, SecurityException {
    Method method = MethodDummy.class.getMethod("publicMethod");
    assertThat(method.getReturnType()).isEqualTo(boolean.class);
    assertThat(method.getParameterCount()).isEqualTo(0);
  }

  @Test
  void test1() throws NoSuchMethodException, SecurityException {
    assertThat(MethodTest.class.getMethods()).filteredOn((x) -> "target".equals(x.getName()))
      .hasSize(1);

    assertThatThrownBy(() -> {
      MethodTest.class.getMethod("5555test44444444");
    }).isInstanceOf(ReflectiveOperationException.class);

  }

  @Test
  void test2() throws Throwable {
    assertThat(MethodDummy.class.getMethod("publicMethod")
      .invoke(new MethodDummy())).isEqualTo(true);

    assertThatThrownBy(() -> {
      MethodDummy.class.getMethod("privateMethod");
    }).isInstanceOf(NoSuchMethodException.class);

    Method privateMethod = MethodDummy.class.getDeclaredMethod("privateMethod");
    privateMethod.setAccessible(true);
    assertThat(privateMethod.invoke(new MethodDummy())).isEqualTo(false);

  }

}
