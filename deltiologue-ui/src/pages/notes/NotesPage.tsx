import { useLoaderData } from "react-router-dom";
import { NoteCatalogue, NoteCatalogueItem } from "../../api/types/notes";
import { NoteCatalogueLoaderResult } from "./noteCatalogueLoader";

const renderedItems = (items: NoteCatalogueItem[]) => {
  return items.map(item => {
    return <li key={item.id}>Key: {item.id} {item.title}</li>;
  });
};

const renderedSection = (idx: string, items: NoteCatalogueItem[]) => {
  return (
    <div>
      {idx}
      <ul>
        {renderedItems(items)}
      </ul>
    </div>
  );
};

const renderedCatalogue = (cat: NoteCatalogue) => {
  const sections = Object.keys(cat).slice(0, 2).map(key => renderedSection(key, cat[key]));
  return (
    <div>{sections}</div>
  );
};

export default function NotesPage() {

  const { catalogue } = useLoaderData() as NoteCatalogueLoaderResult;

  return (
    <div>
      {renderedCatalogue(catalogue)}
    </div>);
}
