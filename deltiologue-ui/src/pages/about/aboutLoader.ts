import { getAboutContent } from "../../api/queries/getAboutContent";
import { AboutSection } from "../../api/types/about";

export interface AboutLoaderResult {
  data: AboutSection[];
}

export async function aboutLoader(): Promise<AboutLoaderResult> {

  const data = await getAboutContent();

  return { data };
}

