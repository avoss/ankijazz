import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public isMenuCollapsed = true;

  constructor() {
  }

  ngOnInit(): void {
  }

  collapseMenu(): void {
    this.isMenuCollapsed = true;
  }

}
