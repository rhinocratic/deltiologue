import { AboutSection } from "../types/about";

export async function getAboutContent(): Promise<AboutSection[]> {

  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;

  const about = await fetch(
    `${baseUrl}/content/0`
  );

  const links = await fetch(
    `${baseUrl}/content/1`
  );

  const technical = await fetch(
    `${baseUrl}/content/2`
  );

  const data: AboutSection[] =
    [
      await about.json(),
      await links.json(),
      await technical.json()
    ];

  return data;
}