import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompingComponent } from './comping/comping.component';
import { HomeComponent } from './home/home.component';
import { RhythmComponent } from './rhythm/rhythm.component';
import { ScalesComponent } from './scales/scales.component';
import { TheoryComponent } from './theory/theory.component';
import { CagedComponent } from './caged/caged.component';
import { PentatonicsComponent } from './pentatonics/pentatonics.component';
import { ContactComponent } from './contact/contact.component';
import { ToolsComponent } from './tools/tools.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', component: HomeComponent },
  { path: 'scales', component: ScalesComponent },
  { path: 'theory', component: TheoryComponent },
  { path: 'rhythm', component: RhythmComponent },
  { path: 'comping', component: CompingComponent },
  { path: 'caged', component: CagedComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'pentatonics', component: PentatonicsComponent },
  { path: 'tools', component: ToolsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top' } )],
  exports: [RouterModule]
})
export class AppRoutingModule { }
