export type ExcludeFromTuple<T extends unknown[], U> = {
  [K in keyof T]: T[K] extends U ? never : T[K];
}[number];

export type Exclusive<T extends PropertyKey[], U = unknown> = T[number] extends infer E
  ? E extends string
  ? Record<E, U> & { [k in ExcludeFromTuple<T, E>]?: never }
  : never
  : never & {};