
export interface NoteCatalogue {
  [index: string]: NoteCatalogueItem[];
}

export interface NoteCatalogueItem {
  id: number;
  title: string;
}
