interface ContentSectionProps {
  title: string;
  content: string;
}

export default function AboutSection({ title, content }: ContentSectionProps) {
  return (
    <div>
      <div>{title}</div>
      <div>{content}</div>
    </div>
  );
}