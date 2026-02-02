export interface CatalogueItem {
  id: string;
  collection_index: string;
  subject_description: string;
  image_thumb: string;
}

export type Catalogue = CatalogueItem[];