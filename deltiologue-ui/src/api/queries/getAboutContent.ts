import { AboutSection } from "../types/about";

export async function getAboutContent(): Promise<AboutSection[]> {

  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;

  const content = await fetch(
    `${baseUrl}/content`
  );

  const data: AboutSection[] = await content.json()

  return data;
}