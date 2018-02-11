package uk.co.ribot.androidboilerplate.ui.main;

import java.util.List;

import ru.macroplus.webplatform.dto.task.TaskDto;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showTask(TaskDto taskDto);

    void showError();

}
