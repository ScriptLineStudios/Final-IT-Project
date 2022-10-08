from abc import ABC, abstractmethod
from typing import List

import pygame

pygame.font.init()


class UIElement(ABC):
    def __init__(self, x: int, y: int) -> None:
        self.x = x
        self.y = y
        self.visible = True
        self.manager = None
        self.returns = None

    def handle_events(self, events: list) -> None:
        pass

    @abstractmethod
    def draw(self, display: pygame.Surface) -> None:
        pass


class GuiManager:
    def __init__(self, gui_elements: List[UIElement], visible_subsections: List[bool]) -> None:
        self.gui_elements = gui_elements
        self.visible_subsections = visible_subsections

        for index, subsection in enumerate(self.gui_elements):
            for element in subsection:
                element.manager = self

    def get_element(self, index: int) -> UIElement:
        """
        Returns the element at the given index
        """

        return self.gui_elements[index]

    def update_visible_subsections(self, new_subsections: List[bool]):
        self.visible_subsections = new_subsections

    def add_element(self, element: UIElement):
        self.gui_elements.append(element)
    
    def remove_element(self, element: UIElement):
        self.gui_elements.remove(element)

    def draw_gui_elements(self, display: pygame.Surface, events: list):
        returns = []
        """
        Draws each ui element
        """
        for index, subsection in enumerate(self.gui_elements):
            if self.visible_subsections[index]:
                for element in subsection:
                    if element.visible:
                        if element.returns:
                            returns.append(element.returns)
                        element.handle_events(events)
                        element.draw(display)
        
        return returns

class Button(UIElement):
    def __init__(self, x: int, y: int, image: str, function, width = None, height = None, should_div=True):
        super().__init__(x, y)

        self.pressing_button = False
        self.function = function

        self.image_name = image
        self.image = pygame.image.load(image)
        self.returns = None

        if width is not None and height is not None:
            self.image = pygame.transform.scale(self.image, (width, height))

        self.should_div = should_div
        
    def __repr__(self):
        return "Button"

    def handle_events(self, events: list) -> None:
        self.returns = None
        for event in events:
            if event.type == pygame.MOUSEBUTTONDOWN:
                if event.button == 1:
                    mx, my = pygame.mouse.get_pos()
                    if self.should_div:
                        mx /= 5
                        my /= 5.3

                    if pygame.Rect(self.x, self.y, self.image.get_width(), self.image.get_height()).collidepoint((mx, my)):
                        self.pressing_button = True
                        self.returns = self.function(self.manager, self)

            if event.type == pygame.MOUSEBUTTONDOWN:
                if event.button == 1:
                    self.pressing_button = True

    def draw(self, display: pygame.Surface) -> None:
        mx, my = pygame.mouse.get_pos()
        display.blit(self.image, (self.x, self.y))


class Text(UIElement):
    def __init__(self, x: int, y: int, text: str, size: int, color=(255, 255, 255)) -> None:
        super().__init__(x, y)
        self.size = size

        self.text = text
        self.regularText = pygame.font.Font('assets/font/font.ttf', self.size)

        self._render_text = self.regularText.render(self.text, False, color)


    def update_text(self, new_text):
        self.text = new_text
        self._render_text = self.regularText.render(self.text, False, color)

    def render_text(self, display, word, pos):
        """for index, letter in enumerate(word):
                img = alphabet[letter]
                if letter == " ":
                    display.blit(img, ((pos[0]+index*32), pos[1]))
                else:
                    display.blit(img, ((pos[0]+index*7), pos[1]))    """

        display.blit(self._render_text, (pos[0], pos[1]))  

    def draw(self, display: pygame.Surface) -> None:
        """
        Renders text
        """
        self.render_text(display, self.text, (self.x, self.y))

class TextBar(UIElement):
    def __init__(self, x: int, y: int, size: int, default_text: str, should_show_text=True):
        super().__init__(x, y)
        self.size = size
        self.string = ""
        self.text = Text(self.x, self.y, self.string, 10)
        self.is_caps = False
        self.shift = False
        self.should_show_text = should_show_text

        self.default_text = default_text
        self.selected = False


        self._text = ""

    def __repr__(self):
        return "Text Bar"

    def handle_events(self, events: list) -> None:
        for event in events:
            if event.type == pygame.KEYDOWN:
                if self.selected:
                    if event.key == pygame.K_BACKSPACE:
                        self.string = self.string[:len(self.string) - 1]
                    elif event.key == pygame.K_CAPSLOCK:
                        self.is_caps = not self.is_caps
                    else:
                        if self.should_show_text:
                            self.string += str(event.unicode).upper() if self.is_caps or self.shift else str(event.unicode).lower() 
                            self._text = self.string
                        else:
                            self.string += "*"
                            self._text += str(event.unicode).upper() if self.is_caps or self.shift else str(event.unicode).lower() 

            if event.type == pygame.MOUSEBUTTONDOWN:
                mx, my = pygame.mouse.get_pos()
                mx /= 5
                my /= 5.3

                if pygame.Rect(self.x, self.y, self.size, 20).collidepoint((mx, my)):
                    for index, subsection in enumerate(self.manager.gui_elements):
                        for element in subsection:
                            if str(element) == "Text Bar":
                                element.selected = False
                    self.selected = not self.selected    

    def get_text(self):
        return self._text 

    def draw(self, display: pygame.Surface) -> None:
        pygame.draw.rect(display, (100, 100, 100), (self.x, self.y, self.size, 20))
        if len(self.string) == 0:
            self.text = Text(self.x, self.y + 3, self.default_text, 10)
            self.text.draw(display)

        else:
            self.text = Text(self.x, self.y + 3, self.string, 10)
            self.text.draw(display)

class Slider(UIElement):
    def __init__(self, x: int, y: int) -> None:
        super().__init__(x, y)

        self.pointer_x = x
        self.pointer_y = y

        self.clicking = False
        self.clicked_mouse_x = 0

    def handle_events(self, events: list) -> None:
        """
        Handles movement of slider
        """

        for event in events:
            if event.type == pygame.MOUSEBUTTONDOWN:
                if event.button == 1:
                    if pygame.Rect(
                        self.pointer_x - 5, self.pointer_y, 10, 10
                    ).collidepoint(pygame.mouse.get_pos()):
                        self.clicking = True
                        self.clicked_mouse_x = pygame.mouse.get_pos()[0]

            if event.type == pygame.MOUSEBUTTONUP:
                if event.button == 1:
                    self.clicking = False

    def get_value(self) -> float:
        clamp = lambda smallest, n, largest: sorted([smallest, n, largest])[1]
        return clamp(-1.0, ((self.pointer_x - self.x) / 100) - 1, 1.0)

    def draw(self, display: pygame.Surface) -> None:
        current_mouse_x = pygame.mouse.get_pos()[0]
        if self.clicking:
            if (
                self.pointer_x < self.x + 99
                and self.clicked_mouse_x - current_mouse_x < 0
                or self.pointer_x > self.x - 1
                and self.clicked_mouse_x - current_mouse_x > 0
            ):
                self.pointer_x -= (self.clicked_mouse_x - current_mouse_x) / 25
        pygame.draw.rect(display, (100, 100, 100), (self.x, self.y, 100, 8))
        pygame.draw.circle(
            display, (255, 0, 0), (self.pointer_x, self.pointer_y + 4), 5
        )