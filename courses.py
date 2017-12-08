class BirkbeckCourse:
    """This class represents a course at Birkbeck."""

    def __init__(self, department, code, title, instructors=None):
        self.department = department
        self.code = code
        self.title = title
        self.students = set()
        self.instructors = list()

        if instructors is None:
            pass
        elif isinstance(instructors, str):
            self.instructors.append(instructors)
        else:
            self.instructors = list(instructors)

    def mark_attendance(self, *students):
        """
        Mark students as present.

        :param *students: Sequence of student names attending the course
        """
        self.students.update(set(students))

    def is_present(self, student):
        """
        Check if student is present in the course.

        :param str student: Student name
        :return: Return True, if student was found in the course
        :rtype: bool
        """
        return student in self.students

    def __eq__(self, value):
        return self.department == value.department and self.code == value.code

    def __le__(self, value):
        return self.code <= value.code


class BirkbeckCSISCourse(BirkbeckCourse):
    """This class represents a CSIS course at Birkbeck."""

    def __init__(self, department, code, title, recorded=False):
        super().__init__(department, code, title)
        self.is_recorded = recorded
