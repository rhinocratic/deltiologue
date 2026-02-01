
export interface NoteCatalogueItem {
  id: number;
  title: string;
}
export interface NoteCatalogue {
  [index: string]: NoteCatalogueItem[];
}
