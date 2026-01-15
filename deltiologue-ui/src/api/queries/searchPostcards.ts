import type { PostcardSummary } from "../types/postcardSummary";

export async function searchPostcards(term: string): Promise<PostcardSummary[]> {
  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;
  const cards = await fetch(
    `${baseUrl}/search/${term}`
  );

  const data: PostcardSummary[] = await cards.json();

  return data;
}