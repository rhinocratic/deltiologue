import { useLoaderData } from "react-router-dom";
import { CatalogueLoaderResult } from "./catalogueLoader";

export default function CataloguePage() {

  const { data } = useLoaderData() as CatalogueLoaderResult;

  const renderedItems = data.map((item) => {
    return (
      <div>
        <div>{item.id}</div>
        <div>{item.description}</div>
        <div>
          <img
            src={`/images/postcards/${item.id}/thumb.jpg`}
            alt={item.image.alt}
          />
        </div>

      </div>
    );
  });

  return (
    <div>
      CataloguePage
      {renderedItems}
    </div>
  );
}