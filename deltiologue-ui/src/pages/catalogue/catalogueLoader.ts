import { getCatalogue } from "../../api/queries/getCatalogue";
import { Catalogue } from "../../api/types/catalogue";

export interface CatalogueLoaderResult {
  data: Catalogue;
}

export async function catalogueLoader(): Promise<CatalogueLoaderResult> {

  const data = await getCatalogue();

  return { data };
}