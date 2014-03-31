package com.appspot.foodsorce.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class SocialMediaPanel extends HorizontalPanel {

	private HTML twitterButton;
	private HTML facebookButton;
	
	public SocialMediaPanel() {
		setSpacing(10);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		twitterButton = new HTML("<iframe allowtransparency=\"true\" show_count=\"false\" show_screen_name=\"false\" show_count=\"false\" frameborder=\"0\" scrolling=\"no\" src=\"//platform.twitter.com/widgets/follow_button.html?screen_name=FoodSorce\" style=\"width:200px; height:20px;\"></iframe>");
		facebookButton = new HTML("<iframe src=\"//www.facebook.com/plugins/like.php?href=https%3A%2F%2Fwww.facebook.com%2Fpages%2FFoodsorce%2F1436352406603396&amp;width=100&amp;layout=button_count&amp;action=like&amp;show_faces=false&amp;share=false&amp;height=21\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; overflow:hidden; width:100px; height:21px;\" allowTransparency=\"true\"></iframe>");
		add(twitterButton);
		add(facebookButton);
		setCellVerticalAlignment(twitterButton, HasVerticalAlignment.ALIGN_BOTTOM);
		setCellVerticalAlignment(facebookButton, HasVerticalAlignment.ALIGN_BOTTOM);
		addStyleName("socialMediaPanel");
	}
}
