import { Component, AfterViewInit, Input, Output, EventEmitter } from '@angular/core';
import EditorJS from '@editorjs/editorjs';
import Header from '@editorjs/header';
import List from '@editorjs/list';
import CodeTool from '@editorjs/code';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CourseService } from '../service/course.service';

@Component({
  selector: 'app-create-subtopic',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './create-subtopic.component.html',
  styleUrl: './create-subtopic.component.scss'
})
export class CreateSubtopicComponent implements AfterViewInit {
  editor!: EditorJS;
  subtopicForm: FormGroup;

  @Input() topicId!: number;
  @Output() submitted = new EventEmitter<any>();

  constructor(
    private fb: FormBuilder,
    private courseService: CourseService
  ) {
    this.subtopicForm = this.fb.group({
      title: ['', Validators.required],
      topicId: ['', Validators.required]
    });
  }

  ngAfterViewInit(): void {
    this.editor = new EditorJS({
      holder: 'editorjs',
      tools: {
        header: Header,
        list: List,
        code: CodeTool
      },
      placeholder: 'Write subtopic content here...'
    });
  }

  async onSubmit() {
    if (this.subtopicForm.invalid || !this.editor) return;

    try {
      const editorContent = await this.editor.save();

      const payload = {
        title: this.subtopicForm.value.title,
        topicId: +this.subtopicForm.value.topicId,
        content: editorContent  // or JSON.stringify(editorContent) if backend expects a string
      };

      this.courseService.createSubTopic(payload).subscribe({
        next: (res) => {
          alert('Subtopic created successfully!');
          this.subtopicForm.reset();
          this.editor.clear(); // Optional: clear editor
        },
        error: (err) => {
          console.error('Failed to create subtopic:', err);
          alert('Failed to create subtopic: ' + (err.error?.message || 'Unknown error'));
        }
      });

    } catch (err) {
      console.error('Editor save error:', err);
      alert('Failed to collect editor content.');
    }
  }

  ngOnInit(): void {
    this.subtopicForm.patchValue({ topicId: this.topicId });
  }

  ngOnDestroy(): void {
    if (this.editor) this.editor.destroy();
  }



}
