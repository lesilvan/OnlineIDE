export class Project {
  id: number;
  name: string;

  constructor(id?: number, name?: string) {
    let d = new Date();
    this.id = id || d.getTime();
    this.name = name || "NewProject";
  }
}
