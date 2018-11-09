package crs.home;

class Storage<T> implements IStorage<T> {
  private T value;

  Storage(T _v) {
    value = _v;
  }

  @Override
  public synchronized void store( T _v ) {
    value = _v;
  }

  @Override
  public synchronized T retrieve() {
    return value;
  }
}
