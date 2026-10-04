import {
  MDXEditor,
  headingsPlugin,
  listsPlugin,
  quotePlugin,
  thematicBreakPlugin,
  markdownShortcutPlugin,
  toolbarPlugin,
  UndoRedo,
  BoldItalicUnderlineToggles,
  BlockTypeSelect,
  ListsToggle,
  Separator,
} from '@mdxeditor/editor';
import '@mdxeditor/editor/style.css';

type Props = {
  value: string;
  onChange: (markdown: string) => void;
};

// Older preparations were plain text: "•" bullets and single line breaks.
// Turn them into Markdown so the inline editor keeps their layout.
export function plainTextToMarkdown(text: string): string {
  const lines = text.replace(/\r\n/g, '\n').split('\n').map(l => l.replace(/^\s*•\s*/, '- '));
  const isList = (l: string) => /^(- |\d+\. )/.test(l);
  const out: string[] = [];
  lines.forEach((line, i) => {
    out.push(line);
    const next = lines[i + 1];
    if (next === undefined || line.trim() === '' || next.trim() === '') return;
    if (isList(line) && isList(next)) return;
    out.push('');
  });
  return out.join('\n');
}

export default function PreparationEditor({ value, onChange }: Props) {
  return (
    <div style={{ border: '1px solid #ccc', borderRadius: '4px', background: 'white' }}>
      <MDXEditor
        markdown={plainTextToMarkdown(value)}
        onChange={onChange}
        contentEditableClassName="preparation-editor"
        plugins={[
          headingsPlugin(),
          listsPlugin(),
          quotePlugin(),
          thematicBreakPlugin(),
          markdownShortcutPlugin(),
          toolbarPlugin({
            toolbarContents: () => (
              <>
                <UndoRedo />
                <Separator />
                <BoldItalicUnderlineToggles />
                <Separator />
                <BlockTypeSelect />
                <ListsToggle />
              </>
            ),
          }),
        ]}
      />
    </div>
  );
}
