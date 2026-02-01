import { NoteCatalogue } from "../types/notes";

export async function getNoteCatalogue(): Promise<NoteCatalogue> {

  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;

  const result = await fetch(
    `${baseUrl}/notes`
  );

  const data: NoteCatalogue = await result.json();

  return data;
}