
export interface Image {
  id: string;
  alt: string;
}
export interface CatalogueItem {
  id: string;
  index: string;
  description: string;
  image: Image;
}

export type Catalogue = CatalogueItem[];