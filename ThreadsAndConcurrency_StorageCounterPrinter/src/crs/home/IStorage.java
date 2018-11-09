package crs.home;

interface IStorage<T> {
  void store(T _v);
  T retrieve();
}
