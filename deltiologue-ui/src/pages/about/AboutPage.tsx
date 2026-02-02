import { useLoaderData } from "react-router-dom";
import { AboutLoaderResult } from "./aboutLoader";
import LinksSection from "./sections/LinksSection";
import TechnicalSection from "./sections/TechnicalSection";
import AboutSection from "./sections/AboutSection";

export default function AboutPage() {

  const { data } = useLoaderData() as AboutLoaderResult;
  const references = data[0];
  const links = data[1];
  const technical = data[2];

  return (
    <div>
      <AboutSection title={references.title} content={references.content} />
      <LinksSection title={links.title} content={links.content} />
      <TechnicalSection title={technical.title} content={technical.content} />
    </div>
  );
}