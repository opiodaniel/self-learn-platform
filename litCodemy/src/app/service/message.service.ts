// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class MessageService {

//   constructor() { }
// }


import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private messageSubject = new BehaviorSubject<string | null>(null);
  message$ = this.messageSubject.asObservable();

  setMessage(message: string) {
    this.messageSubject.next(message);
  }

  clearMessage() {
    this.messageSubject.next(null);
  }
}
