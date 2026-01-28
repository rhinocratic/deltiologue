import { useLoaderData } from "react-router-dom";
import { NoteCatalogue, NoteCatalogueItem } from "../../api/types/notes";
import { NoteCatalogueLoaderResult } from "./noteCatalogueLoader";
import { Accordion, AccordionTab } from 'primereact/accordion';
import { Panel } from "primereact/panel";

const renderedItems = (items: NoteCatalogueItem[]) => {
  return items.map(item => {
    return (
      <li key={item.id}>
        <Panel>
          {item.title}
        </Panel>
      </li>
    );
  });
};

const renderedSection = (idx: string, items: NoteCatalogueItem[]) => {
  return (
    <AccordionTab header={idx}>
      <ul>
        {renderedItems(items)}
      </ul>
    </AccordionTab>
  );
};

const renderedCatalogue = (cat: NoteCatalogue) => {
  const sections = Object.keys(cat).slice(0, 2).map(key => renderedSection(key, cat[key]));
  return (
    <Accordion activeIndex={0}>
      {sections}
    </Accordion>
  );
};

export default function NotesPage() {

  const { catalogue } = useLoaderData() as NoteCatalogueLoaderResult;

  return (
    <div>
      {renderedCatalogue(catalogue)}
    </div>);
}
