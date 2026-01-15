import { getNoteCatalogue } from "../../api/queries/getNoteCatalogue";
import { NoteCatalogue } from "../../api/types/notes";

export interface NoteCatalogueLoaderResult {
  catalogue: NoteCatalogue;
}

export async function noteCatalogueLoader(): Promise<NoteCatalogueLoaderResult> {

  const catalogue: NoteCatalogue = await getNoteCatalogue();

  return { catalogue };
}