import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from './home/home.component';
import { ScalesComponent } from './scales/scales.component';
import { TheoryComponent } from './theory/theory.component';
import { RhythmComponent } from './rhythm/rhythm.component';
import { CompingComponent } from './comping/comping.component';
import { CoffeeComponent } from './coffee/coffee.component';
import { CagedComponent } from './caged/caged.component';
import { PentatonicsComponent } from './pentatonics/pentatonics.component';
import { JamCarouselComponent } from './jam-carousel/jam-carousel.component';
import { FretCarouselComponent } from './fret-carousel/fret-carousel.component';
import { ContactComponent } from './contact/contact.component';
import { SmpteCalculatorComponent } from './smpte-calculator/smpte-calculator.component';
import { ToolsComponent } from './tools/tools.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    ScalesComponent,
    TheoryComponent,
    RhythmComponent,
    CompingComponent,
    CoffeeComponent,
    CagedComponent,
    PentatonicsComponent,
    JamCarouselComponent,
    FretCarouselComponent,
    ContactComponent,
    SmpteCalculatorComponent,
    ToolsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
