export interface CourseResponseDTO {
    courseId: number;
    courseTitle: string;
    courseDescription: string;
    courseTopicCount: number;
    courseSubtopicCount: number;
    courseUpvoteCount: number;
    courseDownvoteCount: number;
    courseCommentCount: number;
    courseImg:any;
  }
  


  export interface Course {
  title: string;
  desc: string;
  upvotes: number;
  downvotes: number;
  comments: number;
  icon: string;
  studentsEnrolled: number;
  category: string;
  isNew: boolean;
  progress: number;
}