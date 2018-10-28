package crs.home;

class Tuple<T1,  T2> {
  private T1 v1;
  private T2 v2;
  public Tuple(T1 val1, T2 val2) {
    v1 = val1;
    v2 = val2;
  }
  public T1 get1() {
    return v1;
  }
  public T2 get2() {
    return v2;
  }
}
