import { PostcardDetails } from "../types/postcardDetails";

export async function getPostcardDetails(index: number): Promise<PostcardDetails> {

  const baseUrl = import.meta.env.VITE_POSTCARD_API_BASE_URL;

  const result = await fetch(
    `${baseUrl}/card/${index}/detail`
  );

  const data: PostcardDetails = await result.json();

  return data;
}