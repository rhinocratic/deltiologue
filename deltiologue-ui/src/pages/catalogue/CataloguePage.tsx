import { useLoaderData } from "react-router-dom";
import { CatalogueLoaderResult } from "./catalogueLoader";

export default function CataloguePage() {

  const { data } = useLoaderData() as CatalogueLoaderResult;

  return (
    <div>
      CataloguePage
      {data.length}
    </div>
  );
}