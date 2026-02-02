import { Catalogue } from "../types/catalogue";

export async function getCatalogue(): Promise<Catalogue> {

  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;

  const catalogue = await fetch(
    `${baseUrl}/cards`
  );

  const data: Catalogue = await catalogue.json();

  return data;
}