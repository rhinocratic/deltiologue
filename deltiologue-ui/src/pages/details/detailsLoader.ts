import type { Params } from "react-router-dom";
import { getPostcardDetails } from "../../api/queries/getPostcardDetails";
import { PostcardDetails } from "../../api/types/postcardDetails";

interface LoaderArgs {
  params: Params;
}

export interface DetailsLoaderResult {
  details: PostcardDetails;
}

export async function detailsLoader({ params }: LoaderArgs): Promise<DetailsLoaderResult> {

  const { index } = params;

  if (!index) {
    throw new Error("Index must be provided")
  }

  const details = await getPostcardDetails(parseInt(index));

  return { details };
}